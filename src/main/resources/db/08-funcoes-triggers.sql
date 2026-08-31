CREATE OR REPLACE FUNCTION atualizar_timestamp_coluna()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_cliente_updated_at BEFORE UPDATE ON cliente
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp_coluna();

CREATE TRIGGER update_produto_updated_at BEFORE UPDATE ON produto
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp_coluna();

CREATE TRIGGER update_entrega_updated_at BEFORE UPDATE ON entrega
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp_coluna();

CREATE TRIGGER update_endereco_updated_at BEFORE UPDATE ON endereco
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp_coluna();

CREATE OR REPLACE FUNCTION impedir_alteracao_historico_entrega()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'historico_entrega e append-only e nao pode ser modificado';
END;
$$ language 'plpgsql';

CREATE TRIGGER trg_impedir_alteracao_historico BEFORE UPDATE OR DELETE ON historico_entrega
    FOR EACH ROW EXECUTE FUNCTION impedir_alteracao_historico_entrega();

-- Nota: o trigger "trigger_mudanca_status" ja existe manualmente no banco de producao
-- (provavelmente grava em historico_entrega quando entrega.status muda). O corpo real
-- dessa funcao nao foi recuperado nesta migracao — consulte pg_get_functiondef no banco
-- real antes de recriar este schema do zero em outro ambiente.
