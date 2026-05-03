# Internal
## query existing multiple merchants
curl -X GET \
    --location 'http://localhost:8090/public/merchants/name/mcdonald' | jq .

## query existing single merchant
curl -X GET \
    --location 'http://localhost:8090/public/merchants/name/subway' | jq .

## query merchant don't exist
curl -X GET \
    --location 'http://localhost:8090/public/merchants/name/xitalaotaitai' | jq .

------------------------------------------------------------------------------------
# External - Ask Miles
## Check connectivity
curl -X GET \
    --location 'https://www.ask-miles.com/api/store' | jq .