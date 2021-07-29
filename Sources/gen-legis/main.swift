import Foundation

fileprivate let skipWords : Set<String> = [ 
   "br", "a", "an", "the", "to", "it", "in", "are", "for", "of", "is",
   "and"
]

fileprivate let letterValues: [Character: Int] = [
   "-": 0,
   "A": 1 ,  "L": 2 ,  "W": 3 ,  "H": 4 ,  "S": 5 ,  "D": 6 ,  "O": 7 ,  "Z": 8 ,  "K": 9 , 
   "V": 10 , "G": 11 , "R": 12 , "C": 13 , "N": 14 , "Y": 15 , "J": 16 , "U": 17 , "F": 18 , 
   "Q": 19 , "B": 20 , "M": 21 , "X": 22 , "I": 23 , "T": 24 , "E": 25 , "P": 26,
   "a": 1 ,  "l": 2 ,  "w": 3 ,  "h": 4 ,  "s": 5 ,  "d": 6 ,  "o": 7 ,  "z": 8 ,  "k": 9 , 
   "v": 10 , "g": 11 , "r": 12 , "c": 13 , "n": 14 , "y": 15 , "j": 16 , "u": 17 , "f": 18 , 
   "q": 19 , "b": 20 , "m": 21 , "x": 22 , "i": 23 , "t": 24 , "e": 25 , "p": 26
]

fileprivate func gematria(for str: String) -> Int {
   return str.reduce(0) { (total,ch) in total + letterValues[ch, default: 0] }
}

// ----------------------------------------------------------------------
// maintain a lexicon of seen words
// ----------------------------------------------------------------------
fileprivate var lexicon : [Int:Set<String>] = [:]

fileprivate func addToLexicon(_ w: String, withValue v: Int) {
   let upper = w.uppercased()
   if lexicon[v]?.insert(upper) == nil {
      lexicon[v] = [upper]
   }
}

// ----------------------------------------------------------------------
// boilerplate for HTML
// ----------------------------------------------------------------------
fileprivate let htmlHeader = """
<!doctype html>
<html>
<head>
   <title>Book of the Law (Gematria Version)</title>
   <style type="text/css" media="all">
     article { font-size: 12pt; max-width: 6in; margin: auto; }
     ol > li > div { vertical-align:bottom; display: inline-flex; flex-direction: column; align-items: center; }
     ol > li > div > span:first-child { height: 10pt; font-size: 10pt; color: #ff0000; }
   </style>
</head>
<body>
<article>
"""

fileprivate let htmlFooter = """
</article>
</body>
</html>
"""

fileprivate func gematriaHTML(for str: String) -> String {
   var buffer : [Substring] = []
   var onPrintable = true
   var total = 0
   var start = str.startIndex
   for idx in str.indices {
      if let value = letterValues[str[idx]] {
          if !onPrintable {
             // We ended a non-printable run...
             buffer.append(str[start..<idx])
             start = idx
             total = 0
             onPrintable = true
          }
          total += value
      } else {
         if onPrintable {
            // We ended a run of printables...
            let printable = String(str[start..<idx])
            if total > 0 && !skipWords.contains(printable) {
               // it seems significant enough to print a total...
               buffer.append("<div><span>\(total)</span>\(printable)</div>") 
               addToLexicon(printable, withValue: total)
            } else {
               // it wasn't significant enough to print a total...
               buffer.append(printable[...])
            }
            onPrintable = false
            start = idx
         }
         if str[idx] == "&" {
            // need to convert ampersands... so print what we had so far
            // and add the entity...
            buffer.append(str[start..<idx])
            buffer.append("&amp;")
            start = str.index(after:idx)
         }
      }
   }

   // need to output whatever was left...
   let remainder = String(str[start...])
   if onPrintable && (total > 0) && !skipWords.contains(remainder) {
       buffer.append("<div><span>\(total)</span>\(remainder)</div>") 
       addToLexicon(remainder, withValue: total)
   } else {
       buffer.append(remainder[...])
   }

   return buffer.joined()
}

// ----------------------------------------------------------------------
// Format the chapters...
// ----------------------------------------------------------------------
fileprivate func formatChapters() {
  for number in 1...3 {
    var buffer : [String] = []

    let chapter = "chap-\(number)"
    guard let url = Bundle.module.url(forResource: chapter, withExtension: "txt") else {
       print("\(chapter) didn't work!")
       continue
    }
    do {
       let data =  try String(contentsOf: url, encoding: .utf8)

       buffer.append(htmlHeader)
       buffer.append("<h1>Chapter \(number)</h1>")
       buffer.append("<ol>")
       data.enumerateLines { (line, _) in
          buffer.append(contentsOf: ["<li>",gematriaHTML(for: line),"</li>\n"])
       }
       buffer.append("</ol>")
       buffer.append(htmlFooter);
       try buffer.joined().write(toFile: chapter+".html", atomically: false, encoding: .utf8)
    } catch {
       print("had an error")
    }
  }
}

// ----------------------------------------------------------------------
// Format the lexicon...
// ----------------------------------------------------------------------
fileprivate func readExtras() {
   guard let url = Bundle.module.url(forResource: "extra-words", withExtension: "txt") else {
      print("extra-words.txt didn't work!")
      return
   }
   do {
      let data =  try String(contentsOf: url, encoding: .utf8)
      data.enumerateLines { (line, _) in
         addToLexicon(line, withValue: gematria(for: line))
      }
   } catch {
      print("had an error with extras!")
   }
}

fileprivate func formatLexicon() {
   var buffer : [String] = []
   do {
      buffer.append(htmlHeader)
      buffer.append("<h1>Lexicon</h1>")
      buffer.append("<dl>\n")
      for k in lexicon.keys.sorted() {
         buffer.append("<dt>\(k)</dt>\n")
         buffer.append("<dl>\(lexicon[k]!.joined(separator: ", "))</dl>\n")
      }
      buffer.append("</dl>")
      buffer.append(htmlFooter)
      try buffer.joined().write(toFile: "lexicon.html", atomically: false, encoding: .utf8)
   } catch {
      print("had an error with lexicon!")
   }
}

// M A I N ....
formatChapters()
readExtras()
formatLexicon()

// vim: set expandtab sw=3 : 
