(ns org-rwtodd.eq-cipher-test
  (:require [clojure.test :refer :all]
            [org-rwtodd.eq-cipher :as eq]))

(deftest test-trivial-cipher
  (binding [eq/*cipher* eq/simple-cipher]
    (testing "trivial cipher results"
      (is (== (eq/calculate-string "abc") 6))
      (is (== (eq/calculate-string "zz") 52)))))
      
(defn- is-all-eq?
  "helper test to calculate across a lot of strings which should be equal to NUM"
  [ num & ws ]
  (is (apply == num (map eq/calculate-string ws))))

(deftest test-alw-cipher
  (binding [eq/*cipher* eq/alw-cipher]
    (testing "alw cipher results"
      (is-all-eq? 46 "woman" "one")
      (is-all-eq? 58 "HADIT" "Libra")
      (is-all-eq? 78 "nuit" "hidden" "fiRE")
      (is-all-eq? 42 "blood" "CROSS" "UFO" "sTAr")
      (is-all-eq? 49 "moon" "KEY")
      (is-all-eq? 68 "Jesus" "LIFE")
      (is-all-eq? 142 "men-in-black" "are-secret")
      (is-all-eq? 200 "manIFESTAtion" "man-if-est-ation"))))

;; ====== end of file
;; Local Variables:
;; page-delimiter: "^;; ======"
;; End:
