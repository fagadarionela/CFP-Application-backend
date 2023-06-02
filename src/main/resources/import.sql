INSERT INTO account VALUES ('expert1', '$2a$10$IgXwqVXXpYhxsGcb1jT8duVIQL.RKFRRHIHqr5EzHriRfNYt2PuZu', 'EXPERT');
-- INSERT INTO account VALUES ('resident1', '$2a$10$IgXwqVXXpYhxsGcb1jT8duVIQL.RKFRRHIHqr5EzHriRfNYt2PuZu', 'RESIDENT');
-- INSERT INTO account VALUES ('resident2', '$2a$10$IgXwqVXXpYhxsGcb1jT8duVIQL.RKFRRHIHqr5EzHriRfNYt2PuZu', 'RESIDENT');
-- INSERT INTO account VALUES ('resident3', '$2a$10$IgXwqVXXpYhxsGcb1jT8duVIQL.RKFRRHIHqr5EzHriRfNYt2PuZu', 'RESIDENT');
-- INSERT INTO account VALUES ('resident4', '$2a$10$IgXwqVXXpYhxsGcb1jT8duVIQL.RKFRRHIHqr5EzHriRfNYt2PuZu', 'RESIDENT');
INSERT INTO account VALUES ('operator1', '$2a$10$IgXwqVXXpYhxsGcb1jT8duVIQL.RKFRRHIHqr5EzHriRfNYt2PuZu', 'OPERATOR');
INSERT INTO account VALUES ('admin1', '$2a$10$IgXwqVXXpYhxsGcb1jT8duVIQL.RKFRRHIHqr5EzHriRfNYt2PuZu', 'ADMIN');

INSERT INTO resident(id,username, grade) VALUES ('b8aa2410-c5ae-11ed-afa1-0242ac120001', 'resident1', 1.00);
INSERT INTO resident(id,username, grade) VALUES ('b8aa2410-c5ae-11ed-afa1-0242ac120002', 'resident2', 1.00);
INSERT INTO resident(id,username, grade) VALUES ('b8aa2410-c5ae-11ed-afa1-0242ac120003', 'resident3', 1.00);
INSERT INTO resident(id,username, grade) VALUES ('b8aa2410-c5ae-11ed-afa1-0242ac120004', 'resident4', 1.00);

INSERT INTO educational_topic VALUES('Normal');
INSERT INTO educational_topic VALUES('Macular conditions');
INSERT INTO educational_topic VALUES('Vascular conditions');
INSERT INTO educational_topic VALUES('Optic nerve conditions');
INSERT INTO educational_topic VALUES('Peripheral retina conditions');
INSERT INTO educational_topic VALUES('Transparent media conditions');

-- INSERT INTO medical_case
-- VALUES ('b8aa2410-c5ae-11ed-afa1-0242ac120002', null, '', null, false, '', 1, 'Ionela', 'Fagadar', 'e, '', '');