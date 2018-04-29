(ns scrabble.generators
  (:require [clojure.test.check.generators :as gen]
            [clojure.string :as str]
            [scrabble.constants :as constants]))

(defn max-frequency [char max-els]
  (fn [word] (<= (get (frequencies word) char 0) max-els)))

;; just take the list of letters per language and use that to generate all the constraints instead
(def jolly-check (max-frequency constants/jolly-char 2))

;; make this more flexible for example could pass things like
;; - ratio of vowels to consonants
;; - average lentgh of words
;; - language
;; And more
(def chars-available-generator
  "Generator of valid word, obtained by creating vector of chars and fmapping join over them"
  (gen/fmap str/join
            (gen/such-that jolly-check
                           (gen/vector
                            (gen/elements (:english constants/alphabet)) 1 scrabble.constants/max-letters))))
