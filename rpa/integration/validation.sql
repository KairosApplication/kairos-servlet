-- Somente leitura; executar APENAS no kairos_normalized_demo dedicado.
BEGIN READ ONLY;
SELECT current_database(), purpose, instance_id, original_schema_sha256
FROM rpa_integration.environment;

SELECT l.source_instance, l.legacy_purchase_id, l.purchase_id AS normalized_purchase_id,
       p.date, p.status, c.company_id, p.customer_id,
       pp.product_id AS normalized_product_id, pp.quantity, pp.amount::numeric AS unit_price,
       l.price_origin, l.synced_at
FROM rpa_integration.purchases l
JOIN Purchases p ON p.id=l.purchase_id JOIN Customers c ON c.id=p.customer_id
JOIN Purchase_Products pp ON pp.purchase_id=p.id
ORDER BY l.source_instance,l.legacy_purchase_id,pp.product_id;

SELECT 'item_company_mismatch' AS check_name,count(*) AS problems
FROM Purchase_Products pp JOIN Purchases p ON p.id=pp.purchase_id
JOIN Customers c ON c.id=p.customer_id JOIN Products pr ON pr.id=pp.product_id
WHERE c.company_id<>pr.company_id
UNION ALL
SELECT 'duplicate_integration_key', count(*) FROM (
    SELECT source_instance,legacy_purchase_id FROM rpa_integration.purchases
    GROUP BY source_instance,legacy_purchase_id HAVING count(*)>1) duplicates
UNION ALL
SELECT 'import_item_count_mismatch',count(*) FROM rpa_integration.purchases l
WHERE l.item_count<>(SELECT count(*) FROM Purchase_Products pp WHERE pp.purchase_id=l.purchase_id)
UNION ALL
SELECT 'unexpected_completed_import',count(*) FROM rpa_integration.purchases l
JOIN Purchases p ON p.id=l.purchase_id WHERE p.status<>'IN_PROGRESS';

SELECT run_id,legacy_purchase_id,purchase_id,status,item_count,reason,occurred_at
FROM rpa_integration.attempts ORDER BY id;
-- Deve ficar vazio: importacao nao equivale a uma venda concluida.
SELECT * FROM vw_bi_sales_14d;
COMMIT;
