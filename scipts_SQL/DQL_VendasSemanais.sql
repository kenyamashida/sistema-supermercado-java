SELECT
    YEARWEEK(data_venda, 1) AS semana, -- O '1' faz a semana começar na Segunda-feira
    MIN(DATE(data_venda)) AS inicio_semana,
    MAX(DATE(data_venda)) AS fim_semana,
    COUNT(id_venda) AS quantidade_de_vendas,
    SUM(valor_total) AS faturamento_total
FROM
    vendas
GROUP BY
    semana
ORDER BY
    semana DESC;