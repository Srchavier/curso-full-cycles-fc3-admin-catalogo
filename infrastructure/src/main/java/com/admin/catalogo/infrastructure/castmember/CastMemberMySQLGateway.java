package com.admin.catalogo.infrastructure.castmember;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.admin.catalogo.domain.castmember.CastMember;
import com.admin.catalogo.domain.castmember.CastMemberGateway;
import com.admin.catalogo.domain.castmember.CastMemberID;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.pagination.SearchQuery;
import com.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import com.admin.catalogo.infrastructure.utils.SpecificationUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;

    public CastMemberMySQLGateway(final CastMemberRepository castMemberRepository) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
    }

    @Override
    public CastMember create(final CastMember aCastMember) {
        return save(aCastMember);
    }

    @Override
    public void deleteById(final CastMemberID aMemberId) {
        final var anId = aMemberId.getValue();
        if (this.castMemberRepository.existsById(anId)) {
            this.castMemberRepository.deleteById(anId);
        }
    }

    @Override
    public Optional<CastMember> findById(final CastMemberID anId) {
        return this.castMemberRepository.findById(anId.getValue())
                .map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public CastMember update(final CastMember aCastMember) {
        return save(aCastMember);
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult =
                this.castMemberRepository.findAll(where, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    private CastMember save(final CastMember aCastMember) {
        return this.castMemberRepository.save(CastMemberJpaEntity.from(aCastMember))
                .toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

    @Override
    public List<CastMemberID> existsByIds(Iterable<CastMemberID> genreIDS) {
          final var ids = StreamSupport.stream(genreIDS.spliterator(), false)
                .map(CastMemberID::getValue)
                .toList();
        return this.castMemberRepository.existsByIds(ids).stream()
                .map(CastMemberID::from)
                .toList();
    }
}
