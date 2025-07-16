SELECT
    DATE(data_venda) AS dia,
    COUNT(id_venda) AS quantidade_de_vendas,
    SUM(valor_total) AS faturamento_total,
    GROUP_CONCAT(DISTINCT forma_pagamento ORDER BY forma_pagamento SEPARATOR ', ') AS formas_de_pagamento
FROM
    vendas
WHERE
    data_venda BETWEEN '2025-06-01 00:00:00' AND '2025-06-08 23:59:59' -- Altere o período aqui
GROUP BY
    dia
ORDER BY
    dia DESC;