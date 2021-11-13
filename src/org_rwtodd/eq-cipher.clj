(ns org-rwtodd.eq-cipher)

;; ====== Cipher Basics
(defn fill-out-cipher
  "take a cipher C containing just capital letters and fill it out the rest of the way."
  [c]
  (assoc (into c (map (fn [[k v]] [(Character/toLowerCase k) v])) c)
         ;; place a zero dash in there if it wasn't there already
         \- (get c \- 0)
         \' (get c \' 0)))

(defn generate-cipher
  "Given a series of numbers, attach them to the letters in order A-Z"
  [nums]
  (fill-out-cipher
   (into {} (map vector (seq "ABCDEFGHIJKLMNOPQRSTUVWZYZ") nums))))

(def alw-cipher
  "The default english qabalah letter values."
  (generate-cipher [1 20 13 6 25 18 11 4 23 16 9 2 21 14 7 26 19 12 5 24 17 10 3 22 15 8]))

(def love-x-cipher
  "an inversion of the ALW cipher -- see New Order of Thelema"
  (generate-cipher [9 20 13 6 17 2 19 12 23 16 1 18 5 22 15 26 11 4 21 8 25 10 3 14 7 24]))

(def liber-cxv-cipher
  "from linda falorio 1978 http://englishqabalah.com/"
  (generate-cipher [1 5 9 12 2 8 10 0 3 6 9 14 6 13 4 7 18 15 16 11 5 8 10 11 6 32]))

(def leeds-cipher
  "1-7-1 1-7-1 cipher (see https://grahamhancock.com/leedsm1/)"
  (generate-cipher (cycle (concat (range 1 7) (range 7 0 -1)))))

(def simple-cipher
  "A-Z 1-26"
  (generate-cipher (range 1 27)))

(def trigrammaton-cipher
  "From R. Leo Gillis TQ (trigrammaton qabalah)"
  (generate-cipher [5 20 2 23 13 12 11 3 0 7 17 1 21 24 10 4 16 14 15 9 25 22 8 6 18 19]))

(def liber-a-cipher
  "Liber A vel Follis https://hermetic.com/wisdom/lib-follis"
  (generate-cipher (range 26)))

(def ^:dynamic *cipher* "The cipher to use for calculations" alw-cipher)

(defn calculate-string
  "Calculate the cipher value of a whole string (regardless of how many words are in it"
  [string]
  (transduce (keep *cipher*) + string))

(defn display-cipher
  "Pretty-print cipher"
  ([] (display-cipher *cipher*))
  ([cipher]
   (dorun
    (map (fn [k sep]
           (print (format "%c: %d%s" k (get cipher k) sep)))
         (concat (seq "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                 (sort (remove #(Character/isLetter %) (keys cipher))))
         (cycle ["\t" "\t" "\t" "\t" "\n"])))
   (println)))

(defn display-cipher-by-number
  "Pretty-print a cipher by numeric value"
  ([] (display-cipher-by-number *cipher*))
  ([cipher]
   (dorun
    (map (fn [[k v] sep]
           (print (format "%c: %d%s" k v sep)))
         (remove #(Character/isLowerCase (key %)) (sort-by val cipher))
         (cycle ["\t" "\t" "\t" "\t" "\n"])))
   (println)))

(defn split-up-string
  "split a string into calculated words and non-words"
  [src]
  (eduction (drop 1)
            (map rest)
            (reductions (fn [[end _ total] part]
                          (let [new-end (+ end (count part))]
                            [new-end (.substring src end new-end) (if (nil? (first part))
                                                                    0
                                                                    (reduce + part))]))
                        [0 "" 0]
                        (eduction (map *cipher*) (partition-by nil?) src))))

;; ====== Move to the html program eventually 
(def skip-words
  "Words that we shouldn't bother annotating."
  #{ "br", "a", "an", "the", "to", "it", "in", "are", "for", "of", "is", "and" })

(defn output-string
  "output a single string, adding non-zero words to the given dict, and
  annotating the non-zero words as we go"
  [dict src]
  (let [out-dict (reduce (fn [dict [s v]]
                           (if (or (zero? v) (skip-words s))
                             (do
                               (print s)
                               dict)
                             (do
                               (print (str "(" s ": " v ")"))
                               (assoc! dict s v))))
                         dict
                         (split-up-string src))]
    (println)
    out-dict))

(defn output-strings
  "output a series of strings, building up a map of all non-zero words"
  ([strs]
   (output-strings {} strs))
  ([orig-dict strs]
   (persistent! (reduce output-string (transient orig-dict) strs))))

(defn output-glossary
  "output words by value"
  [dict]
  (dorun (map (fn [[k vs]]
                (println (format "%04d: %s" k (pr-str vs))))
              
              (into (sorted-map)
                    (map (fn [[k v]]
                           [k (map key v)]))
                    (group-by val dict)))))

;; ====== end of file
;; Local Variables:
;; page-delimiter: "^;; ======"
;; End:
