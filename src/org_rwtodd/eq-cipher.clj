(ns org-rwtodd.eq-cipher)

;; ====== Cipher Basics

(defn fill-out-cipher
  "take a cipher C containing just capital letters and fill it out the rest of the way."
  [c]
  (assoc (into c (map (fn [[k v]] [(Character/toLowerCase k) v])) c)
         ;; place a zero dash in there if it wasn't there already
         \- (get c \- 0)))

(def default-cipher
  "The default english qabalah letter values."
  (fill-out-cipher { \A 1, \L 2,  \W 3,  \H 4,  \S 5,  \D 6,  \O 7,  \Z 8,  \K 9, 
                    \V 10, \G 11, \R 12, \C 13, \N 14, \Y 15, \J 16, \U 17, \F 18, 
                    \Q 19, \B 20, \M 21, \X 22, \I 23, \T 24, \E 25, \P 26 }))


;; ====== Move to the html program eventually 
(def skip-words
  "Words that we shouldn't bother annotating."
  #{ "br", "a", "an", "the", "to", "it", "in", "are", "for", "of", "is", "and" })
