(ns scrabble.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame :refer [reg-sub]]))

(reg-sub
 :name
 (fn [db]
   (:name db)))

(reg-sub
 :current-language
 (fn [db]
   (:language db)))

(reg-sub
 :tile
 (fn [db [_ idx]]
   (get idx (:tiles db) "1")))
