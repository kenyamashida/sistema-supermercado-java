SELECT
    codigo_barras,
    nome,
    quantidade_estoque,
    estoque_minimo,
    (estoque_minimo - quantidade_estoque) AS necessidade_reposicao
FROM
    produtos
WHERE
    quantidade_estoque <= estoque_minimo
ORDER BY
    necessidade_reposicao DESC;