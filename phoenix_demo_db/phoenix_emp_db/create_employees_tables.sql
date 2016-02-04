CREATE TABLE employees (
    emp_no      INTEGER       	NOT NULL,
    birth_date  VARCHAR        	NOT NULL,
    first_name  VARCHAR   		NOT NULL,
    last_name   VARCHAR  		NOT NULL,
    gender      CHAR(1)			NOT NULL,    
    hire_date   VARCHAR        	NOT NULL,
    CONSTRAINT employee_pk PRIMARY KEY (emp_no)
);
CREATE TABLE departments (
    dept_no     CHAR(4)         NOT NULL,
    dept_name   VARCHAR(40)     NOT NULL,
    CONSTRAINT department_pk PRIMARY KEY (dept_no)
);
CREATE TABLE dept_manager (
   dept_no      CHAR(4)         NOT NULL,
   emp_no       INTEGER         NOT NULL,
   from_date    VARCHAR         NOT NULL,
   to_date      VARCHAR         NOT NULL,
   CONSTRAINT dept_manager_pk PRIMARY KEY (dept_no,emp_no)
); 
CREATE TABLE dept_emp (
    emp_no      INTEGER       	NOT NULL,
    dept_no     CHAR(4)         NOT NULL,
    from_date   VARCHAR         NOT NULL,
    to_date     VARCHAR     	NOT NULL,
    CONSTRAINT dept_emp_pk PRIMARY KEY (emp_no,dept_no)
);
CREATE TABLE titles (
    emp_no      INTEGER       	NOT NULL,
    title       VARCHAR(50)     NOT NULL,
    from_date   VARCHAR         NOT NULL,
    to_date     VARCHAR,
    CONSTRAINT titles_pk PRIMARY KEY (emp_no,title,from_date)
); 
CREATE TABLE salaries (
    emp_no      INTEGER        	NOT NULL,
    salary      INTEGER       	NOT NULL,
    from_date   VARCHAR         NOT NULL,
    to_date     VARCHAR    	    NOT NULL,
    CONSTRAINT salaries_pk PRIMARY KEY (emp_no,from_date)
);