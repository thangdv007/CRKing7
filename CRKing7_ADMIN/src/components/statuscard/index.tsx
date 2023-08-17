import React from 'react';
import './style.css';

interface StatusCardProps {
  icon: string;
  count: string;
  title: string;
}

const StatusCard = (props: StatusCardProps) => {
  return (
    <div className="status-card">
      <div className="status-card__icon">
        <i className={props.icon}></i>
      </div>
      <div className="status-card__info">
        <h4>{props.count}</h4>
        <span>{props.title}</span>
      </div>
    </div>
  );
};

export default StatusCard;
