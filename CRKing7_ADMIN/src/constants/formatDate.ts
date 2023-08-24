import { format } from 'date-fns';

export const formatDate = (inputDate: string) => {
  const startDate = new Date(inputDate);
  return format(startDate, 'HH:mm:ss dd-MM-yyyy');
}