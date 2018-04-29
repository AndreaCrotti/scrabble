(ns scrabble.core-test
  (:require [scrabble.core :as sut]
            [clojure.test :refer [deftest is are testing]]
            [scrabble.constants :as const]
            [scrabble.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]))

(deftest test-key-points
  (let [number-letters
        (apply + (map count (vals (:english const/points))))]

    (testing "right number of characters"
      (is (= 27 number-letters)))

    (testing "length is the same"
      (is (= (count (:english const/keyed-points)) number-letters)))))

(deftest test-word-values
  (testing "simple word value"
    (is (= (sut/word-value "test") 4))
    (is (zero? (sut/word-value "")))))

(deftest test-char-value
  (testing "simple char"
    (is (= (sut/char-value {:pos 0 :letter \a :val nil}) 1))
    (is (= (sut/char-value {:pos 0 :letter \a :val :tl}) 3))))

;; could do a simple map of all the possible options
;; and refacotr this mess here
(deftest test-word-values-tiles
  (let [tiles
        [{:pos 0 :letter nil :val nil}
         {:pos 1 :letter \a :val :tw}
         {:pos 2 :letter nil :val :tl}]
        word "b c"]
    (testing "words with positions and numbers"
      (is (= (sut/word-value tiles word) 17))))

  (let [tiles
        [{:pos 0 :letter nil :val :tw}]
        word "b"]
    (testing "one character word"
      (is (= (sut/word-value tiles word) 12))))

  (let [tiles [{:pos 0 :letter \a :val :tw}]
        word  ""]
    (testing "Passing partial information"
      (is (= (sut/word-value tiles word) 1))))

  (let [tiles
        [{:pos 0 :letter \a :val :tw}]
        word "b"]
    (testing "Don't reuse already used triple words"
      (is (= (sut/word-value tiles word) 1)))))

(deftest word-values-test-are
  (are [word-val tiles word] (= word-val (sut/word-value tiles word))
    ;; single letter already filled in
    1 [{:pos 0 :letter \a :val :tw}] "h"))

(deftest str-to-tile-test
  (testing "test str to title"
    (are [tile tile-str] (= tile (sut/str-to-tile tile-str))
      [{:pos 0 :letter \b :val nil}]                                                                  "b"
      [{:pos 0 :letter nil :val :tl}]                                                                 "3"
      [{:pos 0 :letter nil :val :dw}]                                                                 "6"
      [{:pos 0, :letter \c, :val nil} {:pos 1, :letter \a, :val nil} {:pos 2, :letter nil, :val :ol}] "ca1")))

(deftest anagrams-test
  (testing "simple-anagrams"
    (are [ans word] (= (sut/anagrams word) ans)
      #{"ab" "cab" "cb" "ca" "abc" "ba" "bc"} "abc")))

(deftest best-words-test
  (testing "best-words"
    (are [tiles word ans] (= (sut/best-words tiles word) ans)
      "211e1" "friend" '(["fined" 14] ["fired" 13] ["fried" 13] ["finer" 13] ["infer" 10] ["diner" 9])
      ;;FIXME: this should really be '(["cab" 9]) instead of this
      "c11"   "abd"    '())))

(deftest cleanup-dictionary-test
  (let [words ["Hello" "genitive's" "hello"]]
    (is (= (sut/clean-words words) ["genitive" "hello"]))))

;; define what are the various properties
;; 1. given the same tiles and a sequence of letter, any permutation of the letters should give the same result
;; 2. given a number of tiles and letters, the length of any word can not be greater than the union of the letters
;; 3. the total points of a word are always >= the sum of the values of the characters


;; a) check that there can be max 2 jollys at the same time
;; b) could potentially check that the number of letters in each word don't exceed the total potentially

(deftest max-frequency-test
  ;; simple check that the helper function is doing its job
  (are [char max-els word is-max?] (= ((gen/max-frequency char max-els) word) is-max?)
    \* 2 "hello" true
    \* 2 "he**o" true
    \* 2 "h**l*" false))

(defspec points-greater-than-zero
  100
  (prop/for-all [w gen/chars-available-generator]
                (>= (sut/word-value w) 0)))
