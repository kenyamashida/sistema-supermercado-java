SELECT
    p.nome,
    p.preco_venda,
    p.preco_compra,
    (p.preco_venda - p.preco_compra) AS margem_por_unidade,
    SUM(iv.quantidade) AS total_vendido,
    SUM(iv.quantidade * (p.preco_venda - p.preco_compra)) AS lucro_total_por_produto
FROM
    itens_venda AS iv
JOIN
    produtos AS p ON iv.codigo_barras_produto = p.codigo_barras
GROUP BY
    p.nome, p.preco_venda, p.preco_compra
ORDER BY
    lucro_total_por_produto DESC;