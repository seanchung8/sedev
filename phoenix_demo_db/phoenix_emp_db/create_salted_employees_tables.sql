CREATE TABLE employees (
    emp_no      INTEGER       	NOT NULL,
    birth_date  VARCHAR,
    first_name  VARCHAR,
    last_name   VARCHAR,
    gender      CHAR(1),    
    hire_date   VARCHAR,
    CONSTRAINT employee_pk PRIMARY KEY (emp_no)
) SALT_BUCKETS=4;
CREATE TABLE departments (
    dept_no     CHAR(4)         NOT NULL,
    dept_name   VARCHAR(40),
    CONSTRAINT department_pk PRIMARY KEY (dept_no)
) SALT_BUCKETS=4;
CREATE TABLE dept_manager (
   dept_no      CHAR(4)         NOT NULL,
   emp_no       INTEGER		NOT NULL,
   from_date    VARCHAR,
   to_date      VARCHAR,
   CONSTRAINT dept_manager_pk PRIMARY KEY (dept_no,emp_no)
) SALT_BUCKETS=4; 
CREATE TABLE dept_emp (
    emp_no      INTEGER       	NOT NULL,
    dept_no     CHAR(4)         NOT NULL,
    from_date   VARCHAR,
    to_date     VARCHAR,
    CONSTRAINT dept_emp_pk PRIMARY KEY (emp_no,dept_no)
) SALT_BUCKETS=4;
CREATE TABLE titles (
    emp_no      INTEGER       	NOT NULL,
    title       VARCHAR(50)     NOT NULL,
    from_date   VARCHAR         NOT NULL,
    to_date     VARCHAR,
    CONSTRAINT titles_pk PRIMARY KEY (emp_no,title,from_date)
) SALT_BUCKETS=4; 
CREATE TABLE salaries (
    emp_no      INTEGER        	NOT NULL,
    salary      INTEGER,
    from_date   VARCHAR		NOT NULL,
    to_date     VARCHAR,
    CONSTRAINT salaries_pk PRIMARY KEY (emp_no,from_date)
) SALT_BUCKETS=4;
