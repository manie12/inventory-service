package io.inventory_service.datatypes;

public enum ReturnDisposition {
    RESTOCK,        // back to sellable inventory
    REFURBISH,      // needs repair / kitting
    SCRAP,           // dispose, cannot be sold
    RESTOCK_A_GRADE,    // Ready for immediate resale
    RESTOCK_B_GRADE,    // Resalable at discount
    QUARANTINE_RETURN   // Awaiting inspection/final disposition
}
