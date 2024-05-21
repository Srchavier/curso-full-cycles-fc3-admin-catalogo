package com.admin.catalogo.infrastructure.castmember.presenter;

import com.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import com.admin.catalogo.application.castmember.retrieve.list.CastMemberListOutput;
import com.admin.catalogo.infrastructure.castmember.models.CastMemberListResponse;
import com.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {

    static CastMemberResponse present(final CastMemberOutput aMember) {
        return new CastMemberResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString(),
                aMember.updatedAt().toString()
        );
    }

    static CastMemberListResponse present(final CastMemberListOutput aMember) {
        return new CastMemberListResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString()
        );
    }
}
