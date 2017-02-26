(ns scrabble.core-test
  (:require [scrabble.core :as scrabble]
            [clojure.test :as t]
            [scrabble.constants :as const]))

(t/deftest test-key-points
  (let [number-letters
        (apply + (map count (vals (:english const/POINTS))))]

    (t/testing "right number of characters"
      (t/is (= 26 number-letters)))

    (t/testing "length is the same"
      (t/is (= (count (:english const/KEYED-POINTS)) number-letters)))))

(t/deftest test-word-values
  (t/testing "simple word value"
    (t/is (= (scrabble/word-value "test") 4))
    (t/is (zero? (scrabble/word-value "")))))

(t/deftest test-char-value
  (t/testing "simple char"
    (t/is (= (scrabble/char-value {:pos 0 :letter \a :val nil}) 1))
    (t/is (= (scrabble/char-value {:pos 0 :letter \a :val :tl}) 3))))

;; could do a simple map of all the possible options
;; and refacotr this mess here
(t/deftest test-word-values-tiles
  (let [tiles
        [{:pos 0 :letter nil :val nil}
         {:pos 1 :letter \a :val :tw}
         {:pos 2 :letter nil :val :tl}]
        word "b c"]
    (t/testing "words with positions and numbers"
      (t/is (= (scrabble/word-value-tiles tiles word) 17))))

  (let [tiles
        [{:pos 0 :letter nil :val :tw}]
        word "b"]
    (t/testing "one character word"
      (t/is (= (scrabble/word-value-tiles tiles word) 12))))

  (let [tiles [{:pos 0 :letter \a :val :tw}]
        word ""]
    (t/testing "Passing partial information"
      (t/is (= (scrabble/word-value-tiles tiles word) 1))))

  (let [tiles
        [{:pos 0 :letter \a :val :tw}]
        word "b"]
    (t/testing "Don't reuse already used triple words"
      (t/is (= (scrabble/word-value-tiles tiles word) 1)))))


(t/deftest word-values-test-are
  (t/are [word-val tiles word] (= word-val (scrabble/word-value-tiles tiles word))
    ;; single letter already filled in
    1 [{:pos 0 :letter \a :val :tw}] "h"))


(t/deftest str-to-tile-test
  (t/testing "test str to title"
    (t/are [tile tile-str] (= tile (scrabble/str-to-tile tile-str))
      [{:pos 0 :letter \b :val nil}] "b"
      [{:pos 0 :letter nil :val :tl}] "3"
      [{:pos 0 :letter nil :val :dw}] "6"
      [{:pos 0, :letter \c, :val nil} {:pos 1, :letter \a, :val nil} {:pos 2, :letter nil, :val :ol}] "ca1"))
      [{:pos 0, :letter \a, :val nil} {:pos 1, :letter nil, :val :tl}] "a3")

(t/deftest anagrams-test
  (t/testing "simple-anagrams"
    (t/are [ans word] (= (scrabble/anagrams word) ans)
      #{"ab" "cab" "cb" "ac" "ca" "abc" "ba" "bc"} "abc")))


(t/deftest matches-test
  (t/testing "get the best possible placements"
    (t/are [solutions tiles-str letters] (= (scrabble/matches (scrabble/str-to-tile tiles-str) letters false) solutions)
      {"ab" 5, "ba" 5} "11" "ab"
      {"ab" 9 "ba" 6} "12" "ab")))
