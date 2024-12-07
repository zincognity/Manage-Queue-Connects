package control.utils;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import types.Ticket;

public class TicketTable extends AbstractTableModel {
    private ArrayList<Ticket> tickets;

    private String[] columnNames = { "ID", "CLIENTE", "ATENDIDO POR", "FECHA CREACION" };

    public TicketTable(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public int getRowCount() {
        return this.tickets.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ticket ticket = tickets.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return ticket.getName();
            case 1:
                return ticket.getClient().getName() + " " + ticket.getClient().getLastName();
            case 2:
                return ticket.getAtention().getName();
            case 3:
                return ticket.getCreateAtDate();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
    }
}
