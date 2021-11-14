(ns org-rwtodd.eq-cipher.html
  (:require [org-rwtodd.eq-cipher :as eq]
            [clojure.java.io :as io]))

;; ====== Move to the html program eventually 
(def skip-words
  "Words that we shouldn't bother annotating."
  #{ "br", "a", "an", "the", "to", "it", "in", "are", "for", "of", "is", "and" })

(defn output-line
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

(defn output-lines
  "output a series of lines, building up a map of all non-zero words"
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
