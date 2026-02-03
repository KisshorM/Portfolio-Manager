// package com.example.pm_web.repo;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;

// import com.example.pm_web.entity.TransactionsWRTPurse;
// import java.time.LocalDate;



// public interface transactionswrtpurse extends JpaRepository<TransactionsWRTPurse, Long>{
//     List<TransactionsWRTPurse> findByDateOfTransaction(LocalDate dateOfTransaction);
// }



package com.example.pm_web.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.pm_web.entity.TransactionsWRTPurse;

public interface transactionswrtpurse extends JpaRepository<TransactionsWRTPurse, Long> {

    @Query("""
        SELECT t.purseValue
        FROM TransactionsWRTPurse t
        ORDER BY t.dateOfTransaction DESC, t.transaction_id DESC
        LIMIT 1
    """)
    Double getLatestPurseValue();
}
