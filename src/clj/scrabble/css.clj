(ns scrabble.css
  (:require [garden.def :refer [defstyles]]))


(defstyles screen
  ;; various language settings
  [:.language-group {:padding-right "20px"}]
  [:.language.selected {:padding-right "20px"}]
  [:.language.not-selected {:padding-right "20px"}]

  ;; tile settings
  [:.tile-input {:max-width "10px" :padding-right "3px" :font-weight "bold" :border-width "3px"}]
  [:.letter-input {:max-width "8px" :font-weight "extrabold"}]
)
