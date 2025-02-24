package com.admin.catalogo.application.video.retrieve.list;

import com.admin.catalogo.application.UseCase;

import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase
        extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
