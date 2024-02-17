Server IP Address : 100.2.94.121
Server Port: 3306
Global account Username: Sameer , Password: cracras77
Localuser account Username: LocalUser , Password: test12
Schema name: 331table

Data you can use for testing purposes taken from previous exam solution

Question(can copy and paste)
(Q45) Find the difference between the number of students who are gainfully employed -- full-time (a,f), part-time (b,f), and the number of students who are not gainfully employed (d,f), -- and not working (f). 

SQL Solution(can copy and paste)
select cnt_first - cnt_second from (select count(Q45) cnt_first from chad_encoded_data where Q45 in (select distinct response_encoded_value from data_dictionary, response_description where ref_response_description = response_description_pk and question_label = 'Q45' and (response_choice like '%f%a%') union select distinct response_encoded_value from data_dictionary, response_description where ref_response_description = response_description_pk and question_label = 'Q45' and (response_choice like '%f%b%') ) )A, (select count(Q45) cnt_second from chad_encoded_data where Q45 in (select distinct response_encoded_value from data_dictionary, response_description where ref_response_description = response_description_pk and question_label = 'Q45' and (response_choice like '%f%d%') union select distinct response_encoded_value from data_dictionary, response_description where ref_response_description = response_description_pk and question_label = 'Q45' and (response_choice = 'f') ) ) B; 

Criteria requirements for the project

When a user launches the application, the application shall display a menu of three choices: (1) specify a problem, (2) contribute a SQL, and (3) display a list of runnable query.
    -Compile and run the program and you will be prompted to select between 5 options, Specify problem, contribute sql, Display runnable query list, Remove a problem, Exit

When a user selects “specify a problem”, the application shall provide a text input option for the user to enter the description of a query in English.
    -Requirement fullfilled, select 1 and enter a problem to be entered into the database

When a user selects “contribute a SQL”, the application shall display a list of “problem specifications” that did not yet have corresponding SQLs.
    -Requirement fullfilled select 2 and then select and then select a problem ID to solve, none will have solutions

When a user selects a problem specification that does not yet have a corresponding SQL from a given list, the application shall provide an example input (similar to the SQL mapping for hibernate) and a text input option to enter a SQL for the problem specification.
    -when selecting an option you can enter the SQL query to solve it

 (Extra credits) When a user submits a SQL, the application may perform a validation of the SQL.
    -Small attempt, User will not be able to drop tables, insert data, merge data, alter, update, truncate, grant, revoke, call
    Can be tested by typing any keywords with your SQL query

When a user submits a SQL contribution, the application shall store it in MYSQL.
    -Requirement fulfilled, sql queries are stored in the table(see database schema design)

When a user selects “display a list of runnable query”, the application shall display the list and the description of problem specifications that have corresponding SQLs.
    -Requirement fulfilled select option 3 and it will show all problems with solutions given
    
When a user selects a problem specification from the list displayed, the application shall display the parameters needed for the SQL and the input interface for the user to enter a value for each parameter.
    -Requirement fulfilled after selecting 3 you will be prompted to enter a value for each parameter, if condition is failed it will tell you the number of parameters needed.

When a user submits the input parameter values for a selected problem specification, the application shall execute the corresponding SQL against the MYSQL chad_encode d_data.
    -Requirement fulfilled select any sample previously entered data and run it to see the results, Example: run Primary key 2 and enter with 0 parameters(since none is needed)

When MYSQL returns the result of executing a SQL, the application shall display the results to the user.
    -Requirement fulfilled, see the console after executing.

The backend MYSQL shall be accessible by the application anywhere, anytime, on the internet.
    -Requirement fulfilled.

The application shall allow configurations for MYSQL connection using a local or remote credential.
    Requirement fulfilled local and global accounts given.


