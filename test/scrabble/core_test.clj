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
    (is (= (word-value "") 0))))

(deftest test-char-value
  (testing "simple char"
    (is (= (char-value {:pos 0 :letter \a :val nil}) 1))
    (is (= (char-value {:pos 0 :letter \a :val :tl}) 3))))

(deftest test-word-values-tiles
  (let [tiles
        [{:pos 0 :letter nil :val nil}
         {:pos 1 :letter \a :val :tw}
         {:pos 2 :letter nil :val :tl}]
        word "b c"]
    (testing "words with positions and numbers"
      (is (= (word-value-tiles tiles word) 17))))
  (let [tiles
        [{:pos 0 :letter nil :val :tw}]
        word "b"]
    (testing "one character word"
      (is (= (word-value-tiles tiles word) 12))))
  (let [tiles [{:pos 0 :letter \a :val :tw}]
        word ""]
    (testing "Passing partial information"
      (is (= (word-value-tiles tiles word) 1))))
  (let [tiles
        [{:pos 0 :letter \a :val :tw}]
        word "b"]
    (testing "Don't reuse already used triple words"
      (is (= (word-value-tiles tiles word) 1)))))


(deftest word-values-test-are
  (are [word-val tiles word] (= word-val (word-value-tiles tiles word))
    ;; single letter already filled in
    1 [{:pos 0 :letter \a :val :tw}] "h"))


(deftest str-to-tile-test
  (testing "test str to title"
    (are [tile tile-str] (= tile (str-to-tile tile-str))
      [{:pos 0 :letter \b :val nil}] "b"
      [{:pos 0 :letter nil :val :tl}] "3"
      [{:pos 0 :letter nil :val :dw}] "6"
      [{:pos 0, :letter \c, :val nil} {:pos 1, :letter \a, :val nil} {:pos 2, :letter nil, :val :ol}] "ca1"))
      [{:pos 0, :letter \a, :val nil} {:pos 1, :letter nil, :val :tl}] "a3")

(deftest anagrams-test
  (testing "simple-anagrams"
    (are [ans word] (= (anagrams word) ans)
      #{"ab" "cab" "cb" "ac" "ca" "abc" "ba" "bc"} "abc")))
