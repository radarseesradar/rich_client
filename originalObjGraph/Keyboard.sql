CREATE TABLE Assoc_15729533
(
kboid BIGINT NOT NULL,
kboid_ BIGINT NOT NULL,
    PRIMARY KEY (kboid, kboid_)
)
in kbrd04ts01;
CREATE TABLE TVPCa
(
kboid BIGINT NOT NULL,
newlyCreated CHARacter(1),
modified CHARacter(1),
name CLOB(2K),
alias VARCHAR(64),
updateCounter INTeger,
Fk_14286961 BIGINT,
Fk_9044186 BIGINT,
    PRIMARY KEY (kboid)
)
in kbrd32ts02;
CREATE TABLE TVPCb
(
kboid BIGINT NOT NULL,
newlyCreated CHARacter(1),
modified CHARacter(1),
name CLOB(2K),
alias VARCHAR(64),
updateCounter INTeger,
Fk_14286961 BIGINT,
Fk_2883928 BIGINT,
Fk_14680530 BIGINT,
    PRIMARY KEY (kboid)
)
in kbrd32ts02;
CREATE TABLE TVPComposite
(
kboid BIGINT NOT NULL,
newlyCreated CHARacter(1),
modified CHARacter(1),
name CLOB(2K),
alias VARCHAR(64),
updateCounter INTeger,
Fk_14286961 BIGINT,
    PRIMARY KEY (kboid)
)
in kbrd32ts02;
