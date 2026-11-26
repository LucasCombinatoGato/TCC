-- População inicial idempotente das temáticas padrão da plataforma Camora
-- Paleta secundária Camora: #FFBE0B, #FB5607, #FF006E, #8338EC, #3A86FF

INSERT INTO tematica (nome, descricao, cor_hex)
SELECT 'Cultural', 'Exposições, teatro, cinema, literatura, folclore e manifestações artísticas.', '#8338EC'
WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nome = 'Cultural');

INSERT INTO tematica (nome, descricao, cor_hex)
SELECT 'Musical', 'Shows, festivais, apresentações ao vivo, concertos e rodas de música.', '#FF006E'
WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nome = 'Musical');

INSERT INTO tematica (nome, descricao, cor_hex)
SELECT 'Social', 'Ações comunitárias, voluntariado, arrecadações, inclusão e impacto social.', '#FB5607'
WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nome = 'Social');

INSERT INTO tematica (nome, descricao, cor_hex)
SELECT 'Educacional', 'Workshops, palestras, oficinas, cursos gratuitos e feiras do conhecimento.', '#3A86FF'
WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nome = 'Educacional');

INSERT INTO tematica (nome, descricao, cor_hex)
SELECT 'Entretenimento', 'Feiras gastronômicas, encontros geek, lazer ao ar livre e recreação.', '#FFBE0B'
WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nome = 'Entretenimento');

INSERT INTO tematica (nome, descricao, cor_hex)
SELECT 'Ambiental', 'Mutirões ecológicos, sustentabilidade, reflorestamento e causas animais.', '#10B981'
WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nome = 'Ambiental');
