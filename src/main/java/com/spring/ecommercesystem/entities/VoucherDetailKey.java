package com.spring.ecommercesystem.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VoucherDetailKey implements Serializable {
    private Long voucherId;

    private Long userId;

    public VoucherDetailKey() {
    }

    public VoucherDetailKey(Long voucherId, Long userId) {
        this.voucherId = voucherId;
        this.userId = userId;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public VoucherDetailKey setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public VoucherDetailKey setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoucherDetailKey that = (VoucherDetailKey) o;
        return Objects.equals(voucherId, that.voucherId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voucherId, userId);
    }
}
