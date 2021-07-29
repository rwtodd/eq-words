fileprivate let letterValues: [Character: Int] = [
   "A": 1 ,  "L": 2 ,  "W": 3 ,  "H": 4 ,  "S": 5 ,  "D": 6 ,  "O": 7 ,  "Z": 8 ,  "K": 9 , 
   "V": 10 , "G": 11 , "R": 12 , "C": 13 , "N": 14 , "Y": 15 , "J": 16 , "U": 17 , "F": 18 , 
   "Q": 19 , "B": 20 , "M": 21 , "X": 22 , "I": 23 , "T": 24 , "E": 25 , "P": 26,
   "a": 1 ,  "l": 2 ,  "w": 3 ,  "h": 4 ,  "s": 5 ,  "d": 6 ,  "o": 7 ,  "z": 8 ,  "k": 9 , 
   "v": 10 , "g": 11 , "r": 12 , "c": 13 , "n": 14 , "y": 15 , "j": 16 , "u": 17 , "f": 18 , 
   "q": 19 , "b": 20 , "m": 21 , "x": 22 , "i": 23 , "t": 24 , "e": 25 , "p": 26
]

fileprivate func gematria(for str: String) -> Int {
   return str.reduce(0) { (total, ch) in
     return total + letterValues[ch, default: 0]
   }
}

while let ln = readLine() {
   print("\(ln) = \(gematria(for:ln))")
}
