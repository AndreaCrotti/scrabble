(ns scrabble.core-test
  (:require [scrabble.core :as scrabble]
            [clojure.test :as t]
            [scrabble.constants :as const]
            [scrabble.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]))

(t/deftest test-key-points
  (let [number-letters
        (apply + (map count (vals (:english const/POINTS))))]

    (t/testing "right number of characters"
      (t/is (= 27 number-letters)))

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

(t/deftest best-words-test
  (t/testing "best-words"
    (t/are [tiles word ans] (= (scrabble/best-words tiles word) ans)
      "211e1" "friend" '(["fined" 14] ["fired" 13] ["fried" 13] ["finer" 13] ["infer" 10] ["diner" 9])
      ;;FIXME: this should really be '(["cab" 9]) instead of this
      "c11" "abd" '())))


;; define what are the various properties
;; 1. given the same tiles and a sequence of letter, any permutation of the letters should give the same result
;; 2. given a number of tiles and letters, the length of any word can not be greater than the union of the letters
;; 3. the total points of a word are always >= the sum of the values of the characters


;; a) check that there can be max 2 jollys at the same time
;; b) could potentially check that the number of letters in each word don't exceed the total potentially

(t/deftest max-frequency-test
  ;; simple check that the helper function is doing its job
  (t/are [char max-els word is-max?] (= ((gen/max-frequency char max-els) word) is-max?)
    \* 2 "hello" true
    \* 2 "he**o" true
    \* 2 "h**l*" false))

(defspec points-greater-than-zero
  100
  (prop/for-all [w gen/chars-available-generator]
                (>= (scrabble/word-value w) 0)))
