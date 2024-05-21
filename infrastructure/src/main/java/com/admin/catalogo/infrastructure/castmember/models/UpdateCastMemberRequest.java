package com.admin.catalogo.infrastructure.castmember.models;

import com.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType type) {
}
