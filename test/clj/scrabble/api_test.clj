(ns scrabble.api-test
  (:require [scrabble.api :as api]
            [ring.mock.request :as mock]
            [clojure.test :as t]))


(t/deftest test-anagrams
  (t/testing "getting all the anagrams"
    (let [response (api/app (mock/request :get "/"))]
      (t/is (= (:status response) 200))
      (t/is (= (:body response) "Not defined")))))
