sudo -u postgres psql -d postgres_data_source

CREATE TABLE persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) NOT NULL,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL,
    PRIMARY KEY (series)
);

\dt
\dp
GRANT ALL ON TABLE persistent_logins TO postgres_data_source;
\dp