-- 数据库初始化
CREATE DATABASE IF NOT EXISTS stablecoin DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_bin;
USE stablecoin;

-- 1. user — 现实员工
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    employee_no VARCHAR(20) UNIQUE NOT NULL COMMENT '工号',
    password VARCHAR(256) NOT NULL COMMENT '密码（加密存储）',
    name VARCHAR(128) NOT NULL COMMENT '姓名',
    department VARCHAR(128) NOT NULL COMMENT '部门',
    email VARCHAR(256) COMMENT '邮箱',
    phone VARCHAR(32) COMMENT '电话',
    role ENUM('NORMAL','SUPER','ADMIN') DEFAULT 'NORMAL' COMMENT '角色',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    created_time DATETIME COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间',
    last_login DATETIME COMMENT '最后登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='现实员工表';

-- 2. account — 系统账户
CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    account_id VARCHAR(128) UNIQUE NOT NULL COMMENT '余额账号名',
    user_id BIGINT COMMENT '关联用户ID',
    account_type ENUM('PERSONAL','ACTIVITY','BENEFIT','SUPER') NOT NULL COMMENT '账户类型',
    balance BIGINT DEFAULT 0 COMMENT '余额',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否启用',
    created_by BIGINT COMMENT '创建者ID',
    created_time DATETIME COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统账户表';

-- 3. transaction — 交易流水
CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    tx_no VARCHAR(64) UNIQUE NOT NULL COMMENT '流水号',
    source_account_id BIGINT COMMENT '源账户ID',
    target_account_id BIGINT COMMENT '目标账户ID',
    source_name VARCHAR(128) COMMENT '源账户用户名',
    target_name VARCHAR(128) COMMENT '目标账户用户名',
    source_account_type ENUM('PERSONAL','ACTIVITY','BENEFIT','SUPER') COMMENT '源账户类型',
    target_account_type ENUM('PERSONAL','ACTIVITY','BENEFIT','SUPER') COMMENT '目标账户类型',
    amount BIGINT NOT NULL COMMENT '交易金额',
    tx_type ENUM('TRANSFER','GRANT','ACTIVITY_BET','BENEFIT_REDEEM') NOT NULL COMMENT '交易类型',
    reason VARCHAR(256) COMMENT '事由说明',
    created_by BIGINT COMMENT '操作人用户ID',
    related_bet_id BIGINT COMMENT '关联投注ID',
    metadata JSON COMMENT '冗余字段',
    start_time DATETIME COMMENT '交易发起时间',
    end_time DATETIME COMMENT '交易完成时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易流水表';

-- 4. activity — 活动
CREATE TABLE activity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    name VARCHAR(256) NOT NULL COMMENT '活动名称',
    description TEXT COMMENT '活动描述',
    account_id BIGINT NOT NULL COMMENT '活动账户ID',
    cover VARCHAR(256) COMMENT '封面',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    free_credit BIGINT DEFAULT 0 COMMENT '免费额度',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    created_time DATETIME COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- 5. work — 活动作品
CREATE TABLE work (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    activity_id BIGINT NOT NULL COMMENT '活动ID',
    title VARCHAR(256) NOT NULL COMMENT '作品标题',
    authors TEXT COMMENT '作者',
    description TEXT COMMENT '作品描述',
    cover VARCHAR(256) COMMENT '封面',
    link VARCHAR(512) COMMENT '作品详情云文档URL',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    created_time DATETIME COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动作品表';

-- 6. activity_bet — 投注记录
CREATE TABLE activity_bet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    activity_id BIGINT NOT NULL COMMENT '活动ID',
    work_id BIGINT NOT NULL COMMENT '作品ID',
    account_id BIGINT NOT NULL COMMENT '投注账户ID',
    amount BIGINT NOT NULL COMMENT '投注金额',
    used_free_amount BIGINT DEFAULT 0 COMMENT '使用的免费额度',
    related_tx_id BIGINT COMMENT '关联交易ID',
    created_time TIMESTAMP COMMENT '投注时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动投注记录表';

-- 7. benefit — 权益商品
CREATE TABLE benefit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    name VARCHAR(256) NOT NULL COMMENT '权益名称',
    description TEXT COMMENT '权益描述',
    account_id BIGINT UNIQUE NOT NULL COMMENT '余额账号名',
    price BIGINT NOT NULL COMMENT '所需币数',
    image VARCHAR(512) COMMENT '图片URL',
    total INT NOT NULL COMMENT '总数量',
    remain INT NOT NULL COMMENT '剩余数量',
    exp_date DATETIME COMMENT '过期时间',
    active TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    created_by BIGINT COMMENT '创建者ID',
    created_time DATETIME COMMENT '创建时间',
    updated_time DATETIME COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权益商品表';

-- 8. benefit_code — 兑换码
CREATE TABLE benefit_code (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    benefit_id BIGINT NOT NULL COMMENT '对应权益ID',
    code VARCHAR(128) UNIQUE NOT NULL COMMENT '兑换码',
    user_id BIGINT COMMENT '兑换人ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '兑换时间',
    exp_date DATETIME COMMENT '过期时间',
    status ENUM('VALID','REDEEMED') DEFAULT 'VALID' COMMENT '兑换状态',
    redeemed_by BIGINT COMMENT '核销管理员ID',
    redeemed_at DATETIME COMMENT '核销时间',
    metadata JSON COMMENT '冗余字段'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='兑换码表';


-- 插入初始数据到 account 表
INSERT INTO account (account_id,account_type, balance, created_by, created_time, updated_time) 
VALUES 
    ('8619777596981249','ACTIVITY', 0, 1, NOW(), NOW());
-- 插入初始数据到 activity 表
INSERT INTO activity (name, description, account_id, cover, start_time, end_time, free_credit, created_time, updated_time) 
VALUES 
    ('极客大赛', '', 1, 'geek','2025-09-11 00:00:00', '2025-09-12 12:00:00', 100, NOW(), NOW());

-- 插入初始数据到 work 表
INSERT INTO work (activity_id, title, authors, description, cover, link, created_time, updated_time) VALUES 
(1, 'AI智慧工单小助手', 'AIOps联盟——童俊超、胡红青、杨小云(队长)', '本课题聚焦于运维工单“AI+”应用，深度融合“LLM + RAG + 场景化Agent”技术，驱动工单系统向智能化演进。通过工单的自动解析、意图识别及人机协同处置，显著提升工单处理的效率与准确性，实现运维效率与服务质量的跨越式提升。', 'aiops-assistant', '', NOW(), NOW()),
(1, '代码仓库静态分析', '章文斌', '本方案以大模型为引擎：通过单文件智能扫描替代逐行阅读，实现快速解读检视代码；通过Agent+CodeQL构建动态图谱实现复杂函数深度解读、数据流传播分析、数据逆向追溯分析；更将文档与代码向量化融合，构建代码仓库知识库，实现多层次智能问答，让代码仓库的破解势如破竹！', 'repo-analyzer', '', NOW(), NOW()),
(1, '慧问：自主场景化浏览器AI办公助手', '浏览器是第一生产力——罗喆帅、龚志伟(队长)、沈世珺', '浏览器AI办公利器！一键集成，划词速问，慧联场景，问享极致。思绪不断线，办公新体验！', 'browser-ai', '', NOW(), NOW()),
(1, '银行回单标准字段批量智能提取', '模力无限——张宇桐、陈嘉琳(队长)、徐明杰', '基于大模型语义解析能力，实现非标准PDF文件的智能适配与标准字段全自动批量提取，输出结构化数据，高效解决文件格式多样、字段别名复杂等痛点。', 'receipt-parser', '', NOW(), NOW()),
(1, '基于智能体的测试要点分析及测试案例生成', 'Bug Hunter——韦宪哲、曾馨儿(队长)、宋博文', '我们是“缺陷猎手“，专注于深度解析需求，精准生成高覆盖度的测试要点与案例。', 'test-agent', '', NOW(), NOW()),
(1, '北斗客服', 'Easy北斗——陈罗爱新、蒋飞凤(队长)、姜悦', '北斗生态“最强大脑”，一键激活北斗全域能力；终结碎片化问答，重塑一站式AI服务体验', 'beidou-service', '', NOW(), NOW()),
(1, '代码迁移精准解释器', '0 Error——张翠(队长)、张道枫', '代码 “译”栈通，重构更轻松。洞悉代码意，工作更轻松！', 'code-migrator', '', NOW(), NOW()),
(1, '测试环境智能运维服务助手', '王龙川', '打破传统交互模式，提供开发测试环境智能答疑、智能提单与智能运维等自助服务。', 'env-ops-assistant', '', NOW(), NOW()),
(1, 'EAT 电子渠道智能Mock平台', '智冇极限，芯有勥巭——王伟、邱炳蔚(队长)、翟泺琳', '电子渠道智能挡板平台，借助AI大模型与自研能力，消灭“后端阻塞前端”等研发进度阻塞现象，补足智能造数、智能代码分析、智能搜索等智能研发场景。在赋能非功测试、混沌测试基础上，统一收口管理API资产并配置MCP元数据，实现一键转换存量接口对外发布MCP服务，探索电子渠道研发领域人机共研新范式', 'mock-platform', '', NOW(), NOW()),
(1, '工程理解助手', '指南针——钟文清、郭灿灿、张丽(队长)', '将沉默的代码炼为可对话的智慧，让知识的支流汇入决策之河。通过解构系统脉络构建知识，疏通输送管道传递知识，在关键时刻唤醒知识，使工程师专注于创造而非追溯。', 'engineer-helper', '', NOW(), NOW()),
(1, '邮件AI助理', '捉虫队——张翠、张道枫(队长)', '邮件太多无法及时处理？智能提取摘要，秒抓核心；自动识别待办生成督办清单，紧盯进度预警逾期风险。化解信息过载，赋能高效精准决策！', 'mail-assistant', '', NOW(), NOW()),
(1, '基于智能体的信息提取与价格偏离分析', '王之元', '低代码接入，拓展非结构化数据使用场景，提供轻量级、可复用的数据分析方案', 'price-analyzer', '', NOW(), NOW()),
(1, 'PDF/图像解析知识问答助手', '好运连连——丁文倩、李恩雨、曾鉴彬(队长)', '对PDF文档中复杂表格与图像数据的结构化提取与高保真解析，并构建自适应的文档切分算法，显著提升RAG知识库的检索准确性与信息召回率。', 'pdf-qa', '', NOW(), NOW()),
(1, '需求分析助手', '404特战队——沈嘉杰、代起云、雷皓文(队长)', '帮助开发人员更好地理解业务需求，减少开发返工和重复功能开发频率，结合大模型和项目知识文档，提升开发效率。', 'req-analyzer', '', NOW(), NOW()),
(1, '文审通', '码力全开——孙路刚、李天彤(队长)、张东涛', '需求澄清，设计把关，知识即答，一“AI”到底。', 'doc-reviewer', '', NOW(), NOW()),
(1, '金融科技成本智能预测和分析助手', '码上生财——张译方(队长)、周圣杰、高雪灵', '聚焦核心IT系统，通过引入AI分析引擎与结构化监控数据，实现资源使用状况智能评估、性能异常预警、软硬件成本优化建议等功能。', 'cost-analyzer', '', NOW(), NOW()),
(1, 'Sql生成助手', '深智海——郎江涛、彭瑞(队长)、林佳豪', 'SQL生成不用愁，智能助手来解忧，自然语言转脚本，高效精准省时间。', 'sql-generator', '', NOW(), NOW()),
(1, 'Mysql优化助手', '数据调音师——陈飞(队长)、高雪妍', 'AI驱动的Mysql优化助手，利用大数据技术实现SQL review，解决开发运维效率问题。', 'mysql-optimizer', '', NOW(), NOW()),
(1, '一种基于AI Agent的基础知识库问答快速构建方案', '高丰韡', '通过AI Agent技术的引入，探索高精度知识库/RAG的快速建立和迭代方案，并应用于智能问答验证其有效性。', 'kb-builder', '', NOW(), NOW()),
(1, '工作量评估助手', '嵇雅娟', 'WBS优化 × 原子知识库 × 历史数据融合的创新方案。AI不只是替代人的评估，而是让人的经验更有价值，让对的经验被复用，让错的偏差被修正。', 'work-estimator', '', NOW(), NOW());
