(ns scrabble.views-test
  (:require [scrabble.views :as views]
            [cljs.test :as t :include-macros true]))


(t/deftest main-panel-test
  (t/is (pos? (count ((views/main-panel))))))
