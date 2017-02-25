(ns scrabble.api-test
  (:require [scrabble.api :as sut]
            [ring.mock.request :as mock]
            [clojure.test :as t]))


(t/deftest test-anagrams
  (t/testing "getting all the anagrams"
    (let [response (sut/app (mock/request :get "/"))]
      (t/is (= (:status response) 200))
      (t/is (= (:body response) "Not defined")))))
