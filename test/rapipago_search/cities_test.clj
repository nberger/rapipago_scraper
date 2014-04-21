(ns rapipago-search.cities-test
  (:require [clojure.test :refer :all]
            [vcr-clj.clj-http :refer [with-cassette]]
            [rapipago-search.cities :as cities]))

(deftest find-in-province
  (with-cassette :cities-in-salta
    (def salta {:id "A" :name "Salta"})
    (def salta-cities (cities/find-in-province salta))
    (is (> (count salta-cities) 5))
    (is (every? (->> salta-cities (map :name) set) ["CAFAYATE" "EMBARCACION"]))))


