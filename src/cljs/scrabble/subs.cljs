(ns scrabble.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [reg-sub]]))

(reg-sub
 :current-language
 :language) ;; using the fact that keywords are also functions

(reg-sub
 :tile
 (fn [db [_ idx]]
   (get idx (:tiles db) "1")))

(reg-sub
 :letter
 (fn [db [_ idx]]
   (get idx (:letters db) "")))

(reg-sub
 :results
 :results)

(reg-sub
 :word-to-anagram
 :word-to-anagram)

(reg-sub
 :anagrams
 (fn [db _]
   (reverse (sort-by count (:anagrams db)))))
