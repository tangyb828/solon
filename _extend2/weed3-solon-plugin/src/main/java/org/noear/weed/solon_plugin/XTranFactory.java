package org.noear.weed.solon_plugin;

import org.noear.solon.XUtil;
import org.noear.solon.annotation.XTran;
import org.noear.solon.core.Aop;
import org.noear.solon.core.Tran;
import org.noear.weed.DbContext;

import java.util.function.Function;

public class XTranFactory implements Function<XTran, Tran> {
    @Override
    public Tran apply(XTran tran) {
        if (tran.multisource()) {
            return new XTranGroupImp();
        } else {
            if(XUtil.isEmpty(tran.value())){
                throw  new RuntimeException("Please configure @XTran value");
            }

            DbContext db = null;
            if(XUtil.isEmpty(tran.value())){
                //根据名字获取
                db = Aop.get(DbContext.class);
            }else{
                //根据类型获取
                db = Aop.get(tran.value());
            }

            if(XUtil.isEmpty(tran.value())){
                throw  new RuntimeException("@XTran annotation failed");
            }

            return new XTranImp(db);
        }
    }
}