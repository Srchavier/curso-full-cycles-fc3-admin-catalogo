package com.admin.catalogo.infrastructure.castmember.models;

import com.admin.catalogo.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}
