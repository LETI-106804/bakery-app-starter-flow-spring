package com.vaadin.starter.bakery.backend.data;

import java.util.Locale;

import com.vaadin.flow.shared.util.SharedUtil;

/**
 * Representa os diferentes estados possíveis de um pedido na aplicação.
 *
 * <p>Os estados permitem acompanhar o ciclo de vida do pedido desde a criação
 * até à sua conclusão, incluindo possíveis situações de cancelamento ou problemas.</p>
 */
public enum OrderState {

    /**
     * Estado inicial de um pedido recém-criado, ainda não confirmado.
     */
    NEW,

    /**
     * Estado de um pedido que já foi confirmado e está a ser processado.
     */
    CONFIRMED,

    /**
     * Estado de um pedido que foi preparado e está pronto para ser entregue.
     */
    READY,

    /**
     * Estado de um pedido que já foi entregue ao cliente.
     */
    DELIVERED,

    /**
     * Estado que indica que houve um problema no processamento ou entrega do pedido.
     */
    PROBLEM,

    /**
     * Estado de um pedido que foi cancelado antes da entrega.
     */
    CANCELLED;

    /**
     * Devolve uma versão do identificador do estado num formato legível por humanos.
     *
     * <p>Por exemplo: {@code NEW} será convertido para {@code New}.</p>
     *
     * @return o nome do estado formatado de forma legível
     */
    public String getDisplayName() {
        return SharedUtil.capitalize(name().toLowerCase(Locale.ENGLISH));
    }
}
