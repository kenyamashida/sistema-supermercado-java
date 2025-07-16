SELECT
    p.codigo_barras,
    p.nome,
    SUM(iv.quantidade * iv.preco_unitario) AS faturamento_gerado
FROM
    itens_venda AS iv
JOIN
    produtos AS p ON iv.codigo_barras_produto = p.codigo_barras
GROUP BY
    p.codigo_barras, p.nome
ORDER BY
    faturamento_gerado DESC
LIMIT 10;