SELECT
    p.codigo_barras,
    p.nome,
    SUM(iv.quantidade) AS total_unidades_vendidas
FROM
    itens_venda AS iv
JOIN
    produtos AS p ON iv.codigo_barras_produto = p.codigo_barras
GROUP BY
    p.codigo_barras, p.nome
ORDER BY
    total_unidades_vendidas DESC
LIMIT 10; -- Altere o LIMIT para ver mais ou menos produtos