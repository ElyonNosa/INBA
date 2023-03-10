```mermaid
graph TD
    A[3-tier Architecture]
    A --> PR{Presentation}
    A --> L{Logic}
    A --> PE{Persistence}

    PR --> |res| R[pkg layout]
    R --> ACTD(activity_dashboard.xml)
    R ---> ACTT(activity_transactions.xml)
    R --> ACTL(activity_login.xml)
    R ---> ACTP(activity_profile.xml)
    R --> ACTS(activity_settings.xml)
    R ---> ACTTH(activity_threshold.xml)
    R --> ACTVT(activity_view_transaction.xml)
    PR ----> |comp3350.inba| PRES[pkg presentation]
    PRES --> DACT(DashboardActivity.java)
    PRES ---> TACT(TransactionsActivity.java)
    PRES --> LACT(LoginActivity.java)
    PRES ---> PACT(ProfileActivity.java)
    PRES --> SACT(SettingsActivity.java)
    PRES ---> THACT(ThresholdActivity.java)
    PRES --> VTACT(ViewTransactionActivity.java)

    L --> |comp3350.inba| BUS[pkg business]
    BUS --> AT(AccessTransactions.java)
    L --> |comp3350.inba| OBJ[pkg objects]
    OBJ --> TRANS(Transaction.java)
    OBJ ---> CAT(Category.java)
    OBJ --> USER(User.java)

    PE ----> |comp3350.inba| PER[pkg persistence]
    PER --> STUB[pkg stubs]
    PER ---> HSQLDB[pkg hsqldb]
    STUB --> TPS(TransactionPersistenceStub.java)
    HSQLDB --> TPH(TransactionPersistenceHSQLDB.java)
    HSQLDB --> UPH(UserPersistenceHSQLDB.java)
    PER --> TP(TransactionPersistence.java)
    PER --> UP(UserPersistence.java)
    PE --> |comp3350.inba| APP[pkg application]
    APP --> SERV(Service.java)
```
