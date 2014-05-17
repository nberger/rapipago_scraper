(ns rapipago-scraper.core
  (require [clojure.string :as string]
           [net.cgrand.enlive-html :as html]
           [clj-http.client :as http]
           [rapipago-scraper.provinces :as provinces]
           [rapipago-scraper.cities :as cities]))

(defn- fetch-search-result
  [page {province :province city :city}]
  (:body (http/post "http://www.rapipago.com.ar/rapipagoWeb/suc-buscar.htm"
                    {:query-params {:pageNum page}
                     :form-params {:provinciaSuc (:id province)
                                   :listType "doPage"
                                   :ciudadSuc (:id city)}})))

(comment
  (def province (second (provinces/find-all)))
  (def city (first (cities/find-in-province province)))
  (fetch-search-result 2 {:province province :city city})
  )

(defn- build-rapipago
  [id-tag name-tag address-tag]
  (let [id (first (:content id-tag))
        name (first (:content name-tag))
        address (first (:content address-tag))]
  {:id id
   :name (string/trim name)
   :address address}))

(defn- parse-rapipagos
  [rapipagos-html-result]
  (let [html-result (html/html-snippet rapipagos-html-result)
        ids (html/select html-result [:.resultadosNumeroSuc])
        names (html/select html-result [:.resultadosTextWhite])
        resultadosText (html/select html-result [:.resultadosText])
        addresses (keep-indexed #(if (even? %1) %2) resultadosText)]
    (map build-rapipago ids names addresses)))

(comment
  (def result (fetch-search-result {:province province :city city}))
  (parse-rapipagos result)
  )

(defn- fetch-rapipagos-page
  [page filter]
  (let [result (fetch-search-result page filter)
        rapipagos (parse-rapipagos result)]
    (map #(merge filter %1) rapipagos)))

(def results-per-page 6)

(defn- search-from-page
  [page filter]
  (when-let [page-result (seq (fetch-rapipagos-page page filter))]
    (lazy-cat page-result (search-from-page (inc page) filter))))

(defn search
  [filter]
  (search-from-page 1 filter))

(comment

  (def province (second (provinces/find-all)))
  (def banfield (first (filter #(= "BANFIELD" (:name %)) (cities/find-in-province province))))
        (fetch-rapipagos-page 2 {:province province :city banfield})
        (second (search {:province province :city banfield}))
        (take 8 (search {:province province :city banfield}))
        (take 9 (search {:province province :city banfield}))
  )
