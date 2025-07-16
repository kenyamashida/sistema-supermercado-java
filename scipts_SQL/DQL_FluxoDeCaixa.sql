SELECT
    DATE(data_venda) AS dia,
    forma_pagamento,
    SUM(valor_total) AS total_recebido
FROM
    vendas
GROUP BY
    dia, forma_pagamento
ORDER BY
    dia DESC, forma_pagamento;