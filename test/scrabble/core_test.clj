(ns scrabble.core-test
  (:require [clojure.test :refer :all]
            [scrabble.core :refer :all]))

(deftest test-key-points
  (let [number-letters
        (apply + (map count (vals points)))]

    (testing "right number of characters"
      (is (= 26 number-letters)))

    (testing "length is the same"
      (is (= (count keyed-points) number-letters)))))

(deftest test-word-values
  (testing "simple word value"
    (is (= (word-value "test") 4))
    (is (= (word-value "") 0)))
  (testing "word with different tiles settings"
    (comment
      (is (= (word-value "test" {0 :tl}) 6)))))
