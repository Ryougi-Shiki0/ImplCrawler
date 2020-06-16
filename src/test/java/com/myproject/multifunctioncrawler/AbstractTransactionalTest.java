package com.myproject.multifunctioncrawler;

import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: chensichong
 * @date: 2020/6/16
 * @time: 17:00
 * Copyright (C) 2018 MTDP All rights reserved
 */
@Transactional
@Rollback
public abstract class AbstractTransactionalTest extends AbstractJunitBootTest {
}
