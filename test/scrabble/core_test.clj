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

(deftest test-word-values-tiles
  (let [tiles
        [{:pos 0 :letter nil :val nil}
         {:pos 1 :letter \a :val :tw}
         {:pos 2 :letter nil :val :tl}]
        word [\b \a \c]]
    (testing "words with positions and numbers"
      (is (= (word-value-tiles tiles word))))))
