- 为什么MySQL 默认隔离级别是 可重复读RR,而不是 提交读RC
  
  | binlog的几种格式 |                            |
  | ---------------- | -------------------------- |
  | statement        | 记录的是修改SQL语句        |
  | row              | 记录的是每行实际数据的变更 |
  | mixed            | statement和row模式的混合   |
  
  - 在MySQL5.0 版本以前，binlog只支持STATEMENT这种格式！而这种格式在读已提交(Read Commited)这个隔离级别下主从复制是有bug的，因此Mysql将可重复读(Repeatable Read)作为默认的隔离级别
  
  - 解决方法：设置为RR隔离级别；将binlog设置为row格式（但是row格式是在5.1版本开始才引入的）
  
  - 由于历史原因，mysql将默认隔离级别设置为RR，保证主从复制不出问题。
  
  - bug示例(此时两个session的隔离级别已设置为 RC),执行完两个事务后，主服务器是先删后插入，从服务器则是 STATEMENT 格式，执行的是先插入后删除。
  
    | session1                   | session2                   |
    | -------------------------- | -------------------------- |
    | begin;                     | begin;                     |
    | delete from t where a < 3; |                            |
    |                            | insert into t values(1,2); |
    |                            | commit;                    |
    | commit;                    |                            |

