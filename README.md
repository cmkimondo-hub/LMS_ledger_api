# LMS_ledger_api
LMS Database File Included. 

Available Endpoints
Request Details
1. ADD ACCOUNT
Endpoint: POST http://127.0.0.1:8083/ledger/v1/accounts
sample payload:
{
    "accountCode": "1008",
    "accountName": "InterestIncome",
    "accountCategory": "Income",
    "accountDescription": "Interest Income",
    "subaccountId": "",
    "currencyId": "1",
    "accountBalance": 100
}
Creates a new account
3. ADD TRANSACTION
Endpoint: POST http://127.0.01:8083/ledger/v1/transaction
sample payload:
{
    "transactiondate": "2026-01-16",
    "doc_no": "4556666228",
    "description": "Loan repayment with interest",
    "currencyCode": "KES",
    "entries": [
        {
            "trx_key": "4556666228-1",
            "accountCode": 1007,
            "debit": 1200,
            "credit": 0
        },
        {
            "trx_key": "4556666228-2",
            "accountCode": 1005,
            "debit": 0,
            "credit": 1000
        },
        {
            "trx_key": "4556666228-3",
            "accountCode": 1008,
            "debit": 0,
            "credit": 200
        }
    ]
}
Records a new transaction in the ledger.

5. GET TRANSACTION HISTORY
Endpoint: POST http://127.0.01:8083/ledger/v1/trxhistory
sample payload:
{
    "accountCode": "1005",
    "startDate": "2026-01-01",
    "endDate": "2026-01-20"
}
Retrieves historical transaction data based on specified filters and criteria.
