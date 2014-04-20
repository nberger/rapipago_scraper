(ns rapipago-search.provinces
  (require [net.cgrand.enlive-html :as html]
           [clj-http.client :as http]
           [clojure.string :as string]
           [rapipago-search.util :refer :all]))

(defn- fetch-home
  "Fetches the home page for RapiPago"
  []
  (html/html-resource
    (java.io.StringReader.
      (:body (http/get "http://www.rapipago.com.ar/rapipagoWeb/index.htm")))))

(defn- province-options
  []
  (html/select (fetch-home) [:select#provinciaSuc :option]))

(defn find-all
  "All provinces where RapiPago has shops"
  []
  (map parse-option
       (filter is-option-present? (province-options))))

(defn find-by-name
  [province-name]
  (filter #(= (string/lower-case province-name)
              (string/lower-case (:name %1)))
          (all)))
