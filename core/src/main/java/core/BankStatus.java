package core;

import java.io.Serializable;

public enum BankStatus implements Serializable {
    PROCESSED,
    PENDING,
    DECLINED
}
